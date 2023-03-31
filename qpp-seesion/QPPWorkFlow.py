import xml.etree.ElementTree as ET

import numpy as np
import pytrec_eval
import json
import topicLevelData

import scipy
import sklearn.feature_selection
from collections import defaultdict


def sortDictByKey(dict1):
    dict2 = {}
    sorted_keys = sorted(map(int, dict1.keys()))
    for key in sorted_keys:
        for key2 in dict1:
            if int(key2) == key:
                dict2[key] = dict1[key2]
    return dict2


def getSessionDoclist(sessionTrackXMLFile):
    session2doclist = defaultdict(set)

    tree = ET.parse(sessionTrackXMLFile)
    root = tree.getroot()

    sessions = root.findall('session')
    for session in sessions:
        sessionid = session.get('num')
        for inter in session.findall('interaction'):
            results = inter.findall('results')[0]
            for result in results.findall('result'):
                try:
                    docid = result.findall('clueweb12id')[0].text
                    session2doclist[sessionid].add(docid)
                except:
                    pass
    return session2doclist


def getTopic2Session(topicSessionMappingFile):
    topic2session = defaultdict(set)
    with open(topicSessionMappingFile) as f:
        for line in f:
            session, topic = line.split()
            topic2session[topic].add(session)
    return topic2session


# print(topic2session)
def getTopic2DocList(sessionDoc, topic2Session):
    topic2doclist = defaultdict(set)
    for topic in topic2Session:
        for s in topic2Session[topic]:
            # print('s:', s)
            set_ = sessionDoc[s]
            topic2doclist[topic] = topic2doclist[topic].union(set_)
    return topic2doclist
    # print(topic2doclist)
#
# numRetDoc = {}
# for key in topic2doclist.keys():
#     numRetDoc[key] = len(topic2doclist[key])

# print("numRetDoc", sortDictByKey(numRetDoc))

def getTopicDocRel(qrelsFile, topicDocList):
    qrels = defaultdict(dict)
    qrelsTopicDoc = defaultdict(set)
    with open(qrelsFile) as f:
        for line in f:
            topic, dummy, doc, rel = line.split()
            qrelsTopicDoc[topic].add(doc)
            qrels[topic][doc] = int(rel)

    # print('q',qrelsTopicDoc)
    qrelsTopicDocUnionDict = defaultdict(set)
    docRel = defaultdict(dict)
    for topic in qrelsTopicDoc:
        qrelsTopicDocUnionDict[topic] = qrelsTopicDoc[topic] & topicDocList[topic]
        for doc in qrelsTopicDocUnionDict[topic]:
            if doc in qrels[topic]:
                docRel[topic][doc] = qrels[topic][doc]
    return docRel

# numRelDoc = {}
# for topic in docRel:
#     num = 0
#     for doc in docRel[topic]:
#         if docRel[topic][doc] > 0:
#             # print(qrels[key])
#             num = num +1
#     numRelDoc[topic] = num
#
# print(numRelDoc)


def computeRecallList(numRetDoc, numRelDoc):
    topicRecallDict = {}
    for key1 in numRetDoc.keys():
        # print("key", key1)
        for key2 in numRelDoc.keys():
            if key1 == key2:
                recall = numRelDoc[key2]/numRetDoc[key2]
                topicRecallDict[key1] = recall
    topicRecallDict = sortDictByKey(topicRecallDict)
    topicRecallList = []
    for topic in topicRecallDict:
        topicRecallList.append(topicRecallDict[topic])
    return topicRecallList





# topicNQCAvgList = topicLevelData.getTopicNQCAvg('queryNQC', 'topic2Session')
# topicNQCMaxList = topicLevelData.getTopicNQCMax('queryNQC', 'topic2Session')
# arr = np.column_stack((topicNQCAvgList, topicRecallList))
# # arr = np.array([topicRecallList, topicScoreList])
# # print(arr)
#
# print("topicNQCAvgList", topicNQCAvgList)
# print("topicRecalllist:", topicRecallList)
# corr = sklearn.feature_selection.r_regression(arr, topicRecallList)
# x = np.array(topicRecallList)
# y = np.array(topicNQCAvgList)
# z = np.array(topicNQCMaxList)
# p = scipy.stats.pearsonr(x, y)
# t = scipy.stats.kendalltau(x, z)
#
# # p = np.corrcoef(x, y)
# # print("pearson:", corr)
# print('rho:', p)
# print('tau:', t)
# # print('p:', p)





