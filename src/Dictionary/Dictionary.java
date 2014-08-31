package Dictionary;

/**
 * Created by spandan on 8/30/14.
 */
public interface Dictionary {

    /* basic dictionary functions will go here */

    public void add(String key);
    public boolean search(String key);
    public long numOfKeys();
    public TrieNode getRoot();
}
