package Dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spandan on 8/30/14.
 */
public class TrieNode {

    private int freq;
    private Map<Character, TrieNode> children = null;

    public TrieNode() {
        this.freq = 0;
        this.children = new HashMap<Character, TrieNode>();
    }

    public TrieNode getChild(Character ch) {
        return this.children.get(ch);
    }

    public void addChild(Character ch, TrieNode trie_node) {
        assert (trie_node != null);
        this.children.put(ch, trie_node);
    }

    public int getFreq() {
        return this.freq;
    }

    public Map<Character, TrieNode> getChildren() {
        return this.children;
    }

    public void incFreq() {
        this.freq++;
    }
}
