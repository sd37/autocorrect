#!/usr/bin/python

from optparse import OptionParser
import os, sys, pexpect, string, math, signal

# Command line color constants
END     = '\033[0m'
HEADER  = '\033[48;5;60m\033[38;5;15m'
TITLE   = '\033[1;30m'
FAIL    = '\033[0;31m'
CRASH   = '\033[1;31m'
SEP     = '\033[0;37m'
PASS    = '\033[0;32m'
GOOD    = '\033[38;5;107m'
BAD     = '\033[38;5;52m'

# Miscellaneous constants
DIVIDER_WIDTH = 80

tests = []
test_process = None

"""
A command to the student program. The class stores a sequence of input lines and a sequence
of expected output lines.
"""
class Command:
    def __init__(self, inputs, outputs):
        self.inputs = inputs
        self.outputs = outputs
    
    def getInput(self):
        return self.inputs

    def getOutput(self):
        return self.outputs

"""
An individual test case.
"""
class Test:

    """
    Creates a test based on the provided arguments.
    """

    def __init__(self, student_executable, suite_file):
        self.description = suite_file
        self.student_exec = student_executable

	    #TODO: If output starts with error then do the expect ImmediateExit
        self.expectImmediateExit = False

        self.args = self.getArguments(suite_file)
        self.commands = [Command(self.getInputs(suite_file), self.getOutputs(suite_file))]

    def getArguments(self, suite_file):
        f = open(suite_file+'/ARGS')
        args = ''
        for line in f:
			args = args + ' ' + line.strip()
	#print args
        return args.strip()

    def getInputs(self, suite_file):
        f = open(suite_file+'/INPUT')
        listOfInputs = []
        for line in f:
		listOfInputs.append(line[:-1])
        return listOfInputs

    def getOutputs(self, suite_file):
        f = open(suite_file+'/OUTPUT')
        listOfOutputs = []
        for line in f:
            listOfOutputs.append(line.strip())
            if "ExpectImmediateExit" == line.strip():
                self.expectImmediateExit = True
        return listOfOutputs

    """
    Runs the test. Returns True if passed, False otherwise.

    @param prompt  the prompt to expect from the user implementation
    @param timeout  time limit on waiting for a response from the student executable
    """
    def run(self, prompt, timeout): #, add
	global test_process
	try:
		passed = True
		print SEP + ( '-' * DIVIDER_WIDTH) + END
		print 'Running test:', self.description

		# Start the student program
		try:
		    if self.student_exec[-4:] == '.jar':
				test_process = pexpect.spawn('java -jar ' + self.student_exec + ' ' + self.args) #test jixuan's jar with this
		    else:
				test_process = pexpect.spawn(self.student_exec + ' ' + self.args)
		    self.test_process = test_process
		    self.test_process.setecho(False)
		except Exception:
		    print CRASH + 'Program crashed on start up' + END
		    passed = False

		try:
		    # If we don't expect immediate exit, we should see the prompt
		    if passed and not self.expectImmediateExit and prompt:
			# Check for prompt
			self.test_process.expect(prompt,timeout=timeout)
		except pexpect.EOF:
		    print CRASH + 'Student program unexpectedly crashed during test' + END
		    passed = False
		except pexpect.TIMEOUT:
		    print BAD + 'Program timed out waiting for prompt \'' + prompt + '\'' + END
		    passed = False
	 
		try:
		    # Only continue if we haven't already failed
		    if passed:
			# Feed the commands to the program
			for c in self.commands:
			    cmd = c.getInput()[0]
			    # Send the command to the student program, getting the response
			    student_output = self.get_output(c, prompt, timeout)
			    #add_output = []
			    # Check to see if there's any additional output
			    #if add:
				#for o in student_output:
				 #   if o.find(add) != -1:
				#	add_output.append(student_output.pop(student_output.index(o)))
		
			    #help autograde check for edge case problems
			    if c.getOutput()[0][0:6] == 'ERROR:' and len(student_output) > 0 and (student_output[0].find('ERROR:') != -1):
				print GOOD + cmd, ' : ', student_output, END
			    elif student_output != c.getOutput():
			        passed = False
			        print BAD + cmd, '  --->  expected: ', c.getOutput(), END
			        print BAD + cmd, '  --->  got:      ', student_output, END
			        #print BAD + cmd, '  --->  extra:    ', add_output, END
			        break
			    else:
			        print GOOD + cmd, ' : ', student_output, END
			        #print GOOD + cmd, ' :  extra:    ', add_output, END
		    # Check exit status if required by test (this will happen when exiting out of the program - most programs keep on loopin')
		    #if passed and self.exit_status != None:
		#	self.test_process.expect( pexpect.EOF, timeout=timeout )
	#		self.test_process.close()
#			# This is some pexpect bullshit. exitstatus is None if the program exited abnormally, otherwise signalstatus is None
#			exit_status = self.test_process.exitstatus if self.test_process.exitstatus != None else self.test_process.signalstatus
#			if int(exit_status) != int(self.exit_status):
#			    print BAD + 'Expected exit status of', str(self.exit_status), 'but got', str(exit_status) + END
#			    passed = False		
		    

		except pexpect.EOF:
		    print CRASH + 'Student program unexpectedly crashed during test' + END
		    passed = False
		except pexpect.TIMEOUT:
		    print BAD + 'Program timed out waiting for EOF' + END
		    passed = False
	       
		print 'Result:', PASS + 'Passed' if passed else FAIL + 'Failed', END
		self.test_process.sendintr()
		return passed
	except KeyboardInterrupt:
		self.test_process.sendintr();
		sys.exit()
	return False

    """
    Runs the given command, returning the output.
    """
    def get_output(self, command, prompt, timeout):
        # Send the command input arr, sep
        for l in command.getInput():
            self.test_process.sendline(l)
        # Wait for the response
        output = ''
        was_eof = False
        while True:
            try:
                output += self.test_process.read_nonblocking(timeout=timeout)
            except pexpect.TIMEOUT:
                break
            except pexpect.EOF:
                was_eof = True
                break
        output = output.split('\r\n')
        possible_prompt = output.pop()
        if possible_prompt != prompt:
            output.append(possible_prompt)
        if was_eof:
            output.append('--EOF--')

        return output

def signal_handler(signal, frame):
    for test in tests:
        if hasattr(test, 'test_process'):
            test.test_process.sendintr()
    test_process.sendintr()
    sys.exit(0)

"""
Method that will go through the suite file and be able to test multiple test suites in one command
"""

def testTheSuite(executable, suite_file, ops):
    # Ensure that both provided files exist
    if not os.path.isdir(suite_file):
        print 'Test suite directory', suite_file, 'does not exist.'
        sys.exit(1)
    if not os.path.isfile(suite_file+'/ARGS'):	
        print 'Test suite file ARGS does not exist.'
        sys.exit(1)
    if not os.path.isfile(suite_file+'/INPUT'):	
        print 'Test suite file INPUT does not exist.'
        sys.exit(1)
    if not os.path.isfile(suite_file+'/OUTPUT'):	
        print 'Test suite file OUTPUT does not exist.'
        sys.exit(1)
    if not os.path.isfile(executable):
        print 'Executable', executable, 'does not exist.'
        sys.exit(1)

    # create tests from the loaded test suite
    #for test_el in xmldict['suite']['tests']:
    #test_info = test_el['test']
    #test = Test(xmldict['suite'], executable, test_info) #['valid_implementation']
    test = Test(executable, suite_file)
    tests.append(test) 

"""
Main method parsers the input test file and then runs tests. Output differs
based on whether the provided test suite is meant to be user-facing or 
ta-facing.
"""
def main():
    signal.signal(signal.SIGINT, signal_handler)
    signal.signal(signal.SIGTERM, signal_handler)
    signal.signal(signal.SIGABRT, signal_handler)
    # Define the arguments
    parser = OptionParser()
    parser.add_option('-p', '--prompt', dest="prompt", help="The prompt used by the executable's cli", default="")
    parser.add_option('-t', '--timeout', dest='timeout', help='The timeout time (seconds) to use when waiting for user script to respond', type='float', default=0.5)
    parser.add_option('-a', '--add', dest="add", help="This prompt and output after this is considered additional and not part of the required answer", default="Ifyouwannabemyloveryougottagetwithmyfriends") 
    (ops,args) = parser.parse_args()

    # Check arguments
    if len(args) < 2:
        print 'Usage:', sys.argv[0], ' <program executable> <list of suite directory>'
        sys.exit(1)

    executable = args[0]
    hasFailedSuite = False

    for counter in xrange(1,len(args)):
        testTheSuite(executable, args[counter], ops)


    # Set some suite variables
    #user_facing = 'user_facing' in xmldict['suite']
    user_facing = True

    # Update the user
    if user_facing:
        print 'Checking your handin...'
    else:
        print 'Loaded test suite for', project
    print ''


    # Run the tests!
    tests_passed = 0
    for test in tests:
        tests_passed += 1 if test.run(ops.prompt, ops.timeout) else 0 #, ops.add

    print END

    # Print summary
    print ('=' * DIVIDER_WIDTH) + END
    print '===== Summary =====\n', str(tests_passed), '/', str(len(tests)), 'tests passed' + END + '\n'
    
    if tests_passed == len(tests):
        print PASS + 'TEST SUITE PASSED' + END
    else:
        print FAIL + 'TEST SUITE FAILED' + END
        hasFailedSuite = True

    if hasFailedSuite:
        print ''
        print 'Your handin failed this test suite. This test suite is designed to check basic functionality.'
        print 'The tests we will use while grading will be more rigorous. We recommend that you try to debug'
        print 'so that you at least passed this suite before handing in.'

if __name__ == '__main__':
    main()
