package Dictionary;

/**
 * Created by spandan on 8/30/14.
 */
public class DictTrieImpl implements Dictionary {

    private TrieNode root = null;
    private long count = 0;

    public DictTrieImpl() {

        this.root = new TrieNode();
        assert (this.root != null);
        this.count = 0;
    }

    @Override
    public void add(String key) {

        TrieNode pcrawl = this.root;
        assert (pcrawl != null);
        this.count++;

        for (int i = 0; i < key.length(); i++) {

            Character ch = key.charAt(i);

            if (pcrawl.getChild(ch) == null) {
                pcrawl.addChild(ch, new TrieNode());
            }

            pcrawl = pcrawl.getChild(ch);
        }

        pcrawl.incFreq();
    }

    @Override
    public boolean search(String key) {

        TrieNode pcrawl = this.root;
        assert (pcrawl != null);

        for (int i = 0; i < key.length(); i++) {

            Character ch = key.charAt(i);

            if (pcrawl.getChild(ch) == null) {
                return false;
            }

            pcrawl = pcrawl.getChild(ch);
        }

        assert pcrawl != null;

        return pcrawl.getFreq() != 0;
    }

    @Override
    public long numOfKeys() {
        return this.count;
    }

    public TrieNode getRoot() {
        return this.root;
    }
}
