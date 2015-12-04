# -*- coding: utf-8 -*-
"""
Created on Thu Dec  3 21:21:19 2015

@author: afg479
"""

import numpy as np
import matplotlib.pyplot as plt
from sklearn import manifold

orig_file = "/Users/afg479/cs224n/cs224n-pa4/data/wordVectors.txt"
new_file  = "/Users/afg479/cs224n/cs224n-pa4/data/postTrainingWordVectors.txt"

# Read the word vector into a NumPy array
orig_wordvec = np.genfromtxt(orig_file, delimiter=' ')
new_wordvec  = np.genfromtxt(new_file,  delimiter=' ')

# Figure out a sample of rows to sample
sample_rate = 0.01
row_sample = np.random.choice([True,False],
                              size=orig_wordvec.shape[0],
                              p=[sample_rate, 1-sample_rate])

# Downsample the rows
orig = orig_wordvec[row_sample,:]
del(orig_wordvec)
new  = new_wordvec[row_sample,:]
del(new_wordvec)

# Create the T-SNE transformer and apply
tsne = manifold.TSNE(n_components=2, random_state=0)
orig_tsne = tsne.fit_transform(orig)
new_tsne  = tsne.fit_transform(new)


plt.scatter(orig_tsne[:,0], orig_tsne[:,1])
plt.xlabel('t-SNE Dimension 1')
plt.ylabel('t-SNE Dimension 2')
plt.title('t-SNE Projection BEFORE Training')

plt.scatter(new_tsne[:,0],  new_tsne[:,1])
plt.xlabel('t-SNE Dimension 1')
plt.ylabel('t-SNE Dimension 2')
plt.title('t-SNE Projection AFTER Training')