import sys, random
import numpy as np

if len(sys.argv) != 4:
    print("USAGE: python random_word_vec.py <vocab_file> <dimension> <output_file>")
    sys.exit(1)

vocab_file = sys.argv[1]
vocab_dim  = int(sys.argv[2])
out_file   = sys.argv[3]

print("Vocab File: " + vocab_file)
print("Vocab Dim: " + str(vocab_dim))
print("Output File: " + out_file)

with open(vocab_file,'r') as f_vocab, open(out_file,'w') as f_out:
    for word in f_vocab:
        # Generate a random normal vector for every vocab word
        vector = np.random.normal(0, 1, size=vocab_dim)
        # .. and write it to the new random word vector file
        f_out.write(' '.join([str(i) for i in vector]) + "\n")

