#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

import re, string, random

from nltk import download
from nltk.tag import pos_tag
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.corpus import twitter_samples, stopwords
from nltk.tokenize import word_tokenize
from nltk import FreqDist, classify, NaiveBayesClassifier

import math

stop_words = stopwords.words('english')


# In[2]:


# download('punkt')
# download('wordnet')
# download('averaged_perceptron_tagger')
# download('stopwords')
# download('twitter_samples')


# In[3]:


def remove_noise(tweet_tokens, stop_words = stop_words):

    cleaned_tokens = []

    for token, tag in pos_tag(tweet_tokens):
        token = re.sub('http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+#]|[!*\(\),]|'                       '(?:%[0-9a-fA-F][0-9a-fA-F]))+','', token)
        token = re.sub("(@[A-Za-z0-9_]+)","", token)

        if tag.startswith("NN"):
            pos = 'n'
        elif tag.startswith('VB'):
            pos = 'v'
        else:
            pos = 'a'

        lemmatizer = WordNetLemmatizer()
        token = lemmatizer.lemmatize(token, pos)

        lowered_token = token.lower()
                 
        if len(token) > 0 and token not in string.punctuation and lowered_token not in stop_words:
            cleaned_tokens.append(lowered_token)
    return cleaned_tokens


# In[4]:


def get_all_words(tokens_list):
    for tokens in tokens_list:
        for token in tokens:
            yield token


# In[5]:


def get_tweets_for_model(tokens_list):
    for tweet_tokens in tokens_list:
        yield dict([token, True] for token in tweet_tokens)


# In[6]:


duck_tweets_df = pd.read_csv('data/Donald-Tweets!.csv')
duck_tweets_df.head()


# In[7]:


temp = duck_tweets_df['Tweet_Text'].apply(lambda row: word_tokenize(row))


# In[8]:


temp = duck_tweets_df['Tweet_Text'].apply(lambda row: remove_noise(word_tokenize(row)))


# In[9]:


temp[0]


# In[10]:


duck_tweets_df['Retweets'].value_counts()


# In[11]:


positive_tweets = twitter_samples.strings('positive_tweets.json')
negative_tweets = twitter_samples.strings('negative_tweets.json')
positive_tweet_tokens = twitter_samples.tokenized('positive_tweets.json')
negative_tweet_tokens = twitter_samples.tokenized('negative_tweets.json')


# In[12]:


positive_cleaned_tokens_list = []
negative_cleaned_tokens_list = []

for tokens in positive_tweet_tokens:
    positive_cleaned_tokens_list.append(remove_noise(tokens))

for tokens in negative_tweet_tokens:
    negative_cleaned_tokens_list.append(remove_noise(tokens))


# In[13]:


all_pos_words = get_all_words(positive_cleaned_tokens_list)

freq_dist_pos = FreqDist(all_pos_words)
print(freq_dist_pos.most_common(10))


# In[14]:


positive_tokens_for_model = get_tweets_for_model(positive_cleaned_tokens_list)
negative_tokens_for_model = get_tweets_for_model(negative_cleaned_tokens_list)

positive_dataset = [(tweet_dict, "Positive") for tweet_dict in positive_tokens_for_model]

negative_dataset = [(tweet_dict, "Negative") for tweet_dict in negative_tokens_for_model]


# In[15]:


positive_dataset


# In[16]:


positive_cutoff = int(math.floor(len(positive_dataset)*3/4))
negative_cutoff = int(math.floor(len(negative_dataset)*3/4))
train_data = positive_dataset[:positive_cutoff] + negative_dataset[:negative_cutoff]
test_data = positive_dataset[positive_cutoff:] + negative_dataset[negative_cutoff:]

classifier = NaiveBayesClassifier.train(train_data)

print("Accuracy is:", classify.accuracy(classifier, test_data))

print(classifier.show_most_informative_features(10))


# In[17]:


def classify_tweet(tweet):
    custom_tokens = remove_noise(word_tokenize(tweet))
    return classifier.classify(dict([token, True] for token in custom_tokens))


# In[18]:


duck_tweets_df['classifcation'] = duck_tweets_df.apply(lambda row: classify_tweet(row['Tweet_Text']), axis=1)


# In[19]:


duck_tweets_df

