# cs224n-pa4

Use the following command to launch the program from the cs224n-pa4/java folder:

`java -Xmx2g -cp "classes:extlib/*" main.Launcher <options>`

The list of possible options, with their defaults, is provided below:

   Option     |  Type  |   Default     |  Description
--------------|--------|---------------|------------------
-train        | String | ../data/train   | Path to the training data
-test         | String | ../data/test    | Path to the testing data
-wordvec      | String | ../data/wordVectors.txt    | Path to the WordVector file
-vocab        | String | ../data/vocab.txt  | Path to the Vocab listing file
-outfile      | String | ../scored.out   | Output file location
-unkword      | String | UUUNKKK         | Word in the vocab and 
-entities     | , Delimited String | O,LOC,MISC,ORG,PER | List of possible output entities
-lambda       | Double | 0.0             | Lambda value for regularization
-learnrate    | Double | 1e-4            | Learning rate for SGD
-iters        | Int    | 10              | Maximum number of iterations
-windowsize   | Int    | 3               | Rolling window size to input to the NN
-wordvecdim   | Int    | 50              | Dimension of the word vector
-learnwordvec | Boolean | false          | 
-lowcaseall   | Boolean | true           | 
-hiddendim    | , Delimited Int | 10     | Size of each hidden layer in the NN
