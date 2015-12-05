# -*- coding: utf-8 -*-
"""
Created on Thu Dec  3 21:21:19 2015

@author: afg479
"""

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from sklearn import manifold

orig_file = "/Users/afg479/cs224n/cs224n-pa4/data/wordVectors.txt"
new_file  = "/Users/afg479/cs224n/cs224n-pa4/data/postTrainingWordVectors.txt"
vocab_file  = "/Users/afg479/cs224n/cs224n-pa4/data/vocab.txt"
train_file  = "/Users/afg479/cs224n/cs224n-pa4/data/train"

# Get a label for every word in the training sample
vocab_map = {}
with open(vocab_file,'r') as f_vocab:
    word_counter = 0
    for word in f_vocab:
        word = word.rstrip()
        if word not in vocab_map:
            vocab_map[word] = word_counter
            word_counter += 1

# Get the entity for each word
word_entity_map = {}
entity_set = set()
with open(train_file,'r') as f_train:
    for line in f_train:
        if line.find('\t') > -1:
            word, entity = line.split("\t")
            if word not in word_entity_map:
                entity = entity.rstrip()
                word_entity_map[word] = entity
                entity_set.add(entity)

entity_list = [x for x in entity_set]

# Convert both maps into an ID-to-ID mapping
id_map = {}
for word in vocab_map:
    word_id = -1
    if word in vocab_map:
        word_id = vocab_map[word]
    entity_id = -1
    if word in word_entity_map:
        entity = word_entity_map[word]
        if entity in entity_list:
            entity_id = entity_list.index(entity)
    id_map[word_id] = entity_id

# Read the word vector into a NumPy array
orig_wordvec = np.genfromtxt(orig_file, delimiter=' ')
new_wordvec  = np.genfromtxt(new_file,  delimiter=' ')

# Turn the ID-to-ID map into a Numpy array
lookup = np.vectorize(lambda(x): id_map[x])
word_ids = np.arange(orig_wordvec.shape[0])
all_entities = lookup(word_ids)

#o_entities = (all_entities[:] == entity_list.index('O'))
#o_sample = np.random.choice([True,False],
#                              size=orig_wordvec.shape[0],
#                              p=[0.005, 1-0.005])
#non_o_sample = np.random.choice([True,False],
#                                size=orig_wordvec.shape[0],
#                                p=[0.05, 1-0.05])
#row_sample = (o_entities * o_sample) + (~o_entities * non_o_sample)

row_sample = np.random.choice([True,False],
                               size=orig_wordvec.shape[0],
                               p=[0.05, 1-0.05])
o_entities = (all_entities[:] == entity_list.index('O'))

row_sample = row_sample * ~o_entities


# Downsample the rows
slim_orig = orig_wordvec[row_sample,:]
del(orig_wordvec)
slim_new  = new_wordvec[row_sample,:]
del(new_wordvec)
slim_entities = all_entities[row_sample]
del(all_entities)



# Create the T-SNE transformer and apply
tsne = manifold.TSNE(n_components=2, random_state=0)
orig_tsne = tsne.fit_transform(slim_orig)
new_tsne  = tsne.fit_transform(slim_new)

plt.scatter(orig_tsne[:,0], orig_tsne[:,1], c=slim_entities)
plt.xlabel('t-SNE Dimension 1')
plt.ylabel('t-SNE Dimension 2')
plt.title('t-SNE Projection BEFORE Training')

plt.scatter(new_tsne[:,0],  new_tsne[:,1], c=slim_entities)
plt.xlabel('t-SNE Dimension 1')
plt.ylabel('t-SNE Dimension 2')
plt.title('t-SNE Projection AFTER Training')
