import java.io.File;
// import java.io.BufferedWriter;
// import java.io.FileWriter;
import java.util.Scanner;
import java.lang.Math;
import java.lang.String;


class Node {
    public int index;
    public String key;
    public String value;

    // constructor
    public Node() {}

    public Node(int i, String k, String v) {
        index = i;
        key = k;
        value = v;
    }
}

class HashTable {
    public int size;
    public Node hashTable[];
    public int filled = 0;

    // constructor
    public HashTable(int size) {
        this.size = size;
        hashTable = new Node[size];
    }

    public boolean isFull() {
        if (filled < size) return false;
        else return true;
    }

    public int hashFunction(String key) {
        long h = 0;
        int num[] = {37, 79, 131};

        for (int i = 0; i < key.length(); i++) {
            int ascii = (int) key.charAt(i);
            h = (h * (long) num[i % 3]) + (long) ascii;
        }

        h = h % Integer.MAX_VALUE;
        if (h < 0) h = -h;
        
        return (int) h;
    }

    public boolean isPrime(int n) {
        boolean prime = true;

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                prime = false;
                break;
            }
        }
        return prime;
    }

    public int getPrimeAboveInt(int i) {
        int p = (i % 2 == 1) ? i : (i + 1);

        while (isPrime(p) == false) { p += 2; }
        return p;
    }

    public void insert(String key, String value) {
        int h = hashFunction(key);
        int index = h % size;

        if (isFull() == true) {
            int newsize = getPrimeAboveInt(size * 2);
            Node temp[] = new Node[newsize];

            for (int i = 0; i < size; i++) {
                temp[i] = hashTable[i];
            }

            hashTable = temp;
            size = newsize;
        }
        
        // Quadratic probing
        int n = 1;
        while (hashTable[index] != null) {
            index = (h + n * n) % size;
            n++;
        }
        
        Node data = new Node(index, key, value);
        hashTable[index] = data;
        filled++;
    }

    public String get(String key) {
        int h = hashFunction(key);
        int index = h % size;

        int n = 1;
        do {
            if (hashTable[index] == null) {
                return "";
            } else if (hashTable[index].value == key) {
                return hashTable[index].value;
            } else {
                index = (h + n * n) % size;
                n++;
            }
        } while (true);
    }
}

public class Anagram {
    private String anagrams[];
    private int ANA_MINSIZE = 8;
    private int a_n = 0, a_size = ANA_MINSIZE;
    private int INIT_SIZE = 201847;
    HashTable Dictionary = new HashTable(INIT_SIZE);

    public void createDictionary() throws Exception {
        // read vocabulary.txt file and insert words into dictionary
        File f = new File("vocabulary.txt");
        Scanner s = new Scanner(f);
        
        while (s.hasNextLine()) {
            String word = s.nextLine();
            Dictionary.insert(word, word);        
        }
        s.close();
    }

    public void writeAnagramsToOutput(String anagrams[], int n) {
        // -------------- CODE TO OUTPUT TO FILE ----------------- //

        // try {
        //     File f = new File("output.txt");
        //     FileWriter fw = new FileWriter(f, true);
        //     BufferedWriter bw = new BufferedWriter(fw);

        //     for (int i = 0; i < n; i++) {
        //         bw.write(anagrams[i]);
        //         bw.newLine();
        //     }

        //     bw.write("-1");
        //     bw.newLine();
        //     bw.close();
        //     fw.close();
        // } catch (Exception e) {
        //     System.out.println("Exception - output.txt");
        // }

        // -------- CODE TO OUTPUT IN TERMINAL DIRECTLY --------- //
        for (int i = 0; i < n; i++) {
            System.out.println(anagrams[i]);
        }
        System.out.println("-1");
    }

    public void addToAnagrams(String a) {
        if (a_n == a_size) {
            String temp[] = new String[a_size * 2];
            for (int i = 0; i < a_n; i++) {
                temp[i] = anagrams[i];
            }
            anagrams = temp;
            a_size *= 2;
        }

        anagrams[a_n] = a;
        a_n++;
    }

    public String swap(String s, int a, int b) {
        if (a == b) return s;
        else {
            String s1 = s.substring(a, a + 1);
            String s2 = s.substring(b, b + 1);
            return s.substring(0, a) + s2 + s.substring(a + 1, b) + s1 + s.substring(b + 1, s.length());
        }
    }

    public void permute(String s, int first) {
        if (first == s.length() - 1) {
            addToAnagrams(s);
        } else {
            for (int i = first; i < s.length(); i++) {
                String newstr = swap(s, first, i);
                permute(newstr, first + 1);
            }
        }
    }

    public void getAnagrams(String s) {
        anagrams = new String[a_size];

        // remove all spaces from s
        String t = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') t += s.substring(i, i + 1);
        }
        s = t;

        // Adding all possible permutations to array anagrams
        permute(s, 0);

        // Removing repetitions if they exist
        String a_temp[] = new String[a_n];
        int b_n = 0;

        for (int i = 0; i < a_n; i++) {
            // check if anagrams[i] is added already
            boolean added = false;
            for (int j = 0; j < b_n; j++) {
                if (a_temp[j] == anagrams[i]) {
                    added = true;
                    break;
                }
            }
            if (added == false) {
                a_temp[b_n] = anagrams[i];
                b_n++;
            }
        }
        anagrams = a_temp;
        a_n = b_n;
        
        // adding anagrams having spaces
        // # single space
        for (int a = 0; a < b_n; a++) {
            String str = anagrams[a];
            for (int i = 1; i < str.length(); i++) {
                String newstr = str.substring(0, i) + " " + str.substring(i, str.length());

                // checking for validity with Dictionary
                if (Dictionary.get(str.substring(0, i)) != "" && Dictionary.get(str.substring(i, str.length())) != "") {
                    addToAnagrams(newstr);
                }
            }
        }
        // # two spaces
        for (int a = 0; a < b_n; a++) {
            String str = anagrams[a];
            for (int i = 1; i < str.length() - 1; i++) {
                for (int j = i + 1; j < str.length(); j++) {
                    String newstr = str.substring(0, i) + " " + str.substring(i, j) + " " + str.substring(j, str.length());
                    
                    // checking for validity with Dictionary
                    if (Dictionary.get(str.substring(0, i)) != "" && Dictionary.get(str.substring(i, j)) != "" && Dictionary.get(str.substring(j, str.length())) != "") {
                        addToAnagrams(newstr);
                    }
                }
            }
        }
        
        // filtering for valid anagrams (the ones without spaces) using Dictionary
        String valid_anagrams[] = new String[a_n];
        int total_anagrams = 0;

        for (int i = 0; i < a_n; i++) {
            if (i < b_n) {
                if (Dictionary.get(anagrams[i]) != "") {
                    valid_anagrams[total_anagrams] = anagrams[i];
                    total_anagrams++;
                }
            } else {
                valid_anagrams[total_anagrams] = anagrams[i];
                total_anagrams++;
            }
        }
        anagrams = valid_anagrams;
        a_n = total_anagrams;

        // sorting the anagrams in lexicographical order
        for (int i = 0; i < a_n - 1; i++) {
            for (int j = i + 1; j < a_n; j++) {
                if (anagrams[i].compareTo(anagrams[j]) > 0) {
                    String x = anagrams[j];
                    anagrams[j] = anagrams[i];
                    anagrams[i] = x;
                }
            }
        }
        
        writeAnagramsToOutput(anagrams, a_n);
        a_n = 0;
        a_size = ANA_MINSIZE;
    }
    
    public void readInput() throws Exception {
        File f = new File("input.txt");
        Scanner s = new Scanner(f);

        int K = (int) Integer.parseInt(s.nextLine());
        int i = 0;

        // ------------ CODE IN CASE OUTPUT FILE TO BE GENERATED ------------
        // create new file named output.txt, or delete if file already exists
        // File o = new File("output.txt");
        // if (o.createNewFile() == false) o.delete();
        
        while (s.hasNextLine() && i < K) {
            getAnagrams(s.nextLine());
            i++;
        }
        s.close();
    }

    public static void main(String[] args) throws Exception {
        Anagram a = new Anagram();
        a.createDictionary();
        a.readInput();
    }
}
