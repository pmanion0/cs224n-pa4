# cs224n-pa4

## Launcher

Launcher is used to run a single configuration. Use the following command to launch the program from the cs224n-pa4/java folder:

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
-learnwordvec | Boolean | false          | Update word vectors during training
-lowcaseall   | Boolean | true           | Ignore case in the word vectors
-hiddendim    | , Delimited Int | 10     | Size of each hidden layer in the NN


## TestConfigLauncher

TestConfigLauncher is used to run a series of tests that are described in a test configuration file. This file allows you to submit a number of closely related configurations to test the impact of different settings. Use the following command to launch the program from the cs224n-pa4/java folder:

`java -Xmx2g -cp "classes:extlib/*" main.TestConfigLauncher <test_config_file>`

The test config file is parsed by the TestConfigurationReader. This reader begins by initializing a default configuration and proceeds to read all user defined settings until a test case delimiter `----` is encountered. At that point, a test run will be created with the settings *as of that point* in the file. This can be done repeatedly to define as many test cases as you would like.

The options are remembered in a rolling manner as the reader goes through the file, so any options defined in one test will carry over into all future tests unless they are explicitly overwritten. 

Options are defined in `<option>: <value>` pairs in the file. White space for these settings are ignored, so make it look readable!

The first line and all test case delimiter lines must begin with `----` (no leading whitespace). However, you can add comments on the same line to describe the test configuration.

All options outlined in the Launcher documentation above are valid here, but **make sure to exclude the - prefix**, e.g. the command line option `-iters` should show up as `iters` below.

####Structure:
```
---- Initial Setup
  <option1>: <value>
  			...
  <optionN>: <value>
---- Test Case #1 Created
  <option1>: <value>
  			...
  <optionN>: <value>
---- Test Case #2 Created
```

####Example:

```
---- Define all general settings for the test
  train:      ../data/train
  test:       ../data/dev
  wordvec:    ../data/wordVectors.txt
  vocab:      ../data/vocab.txt
  lambda:     0.0
  learnrate:  0.001
  windowsize: 5
  iters:      100
---- Create the 1st test run with all options above ^
  lambda:     0.03
---- Create a 2nd similar run but now lambda = 0.03
  learnrate: 0.01
  learnrate: 0.001
---- Create a 3rd similar run but now learnrate = 0.001 (lambda is still 0.03)
```

Notice above that for the 3rd run, the first learnrate setting is overridden by the second learnrate call!