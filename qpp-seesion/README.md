README for python

We implemented Python code to do parts of my project, it also attached on the same folder.
QPPWorkFlow.py -> includes functions for evaluation
computeCorr.py -> main function to get the correlation coefficients and perform the rank correlation
topicLevelData.py -> aggregate the data


We used two QPP methods to evaluate the project: NQC and WIG.

If you want to get their rho and tau coefficient , you need to change the parameter when calling the function, do something like the following example:

# corr = getQPPCorr('avg', 'NQC', 'queryNQC', 'topic2Session', topicRecallList)
# corr = getQPPCorr('max', 'NQC', 'queryNQC', 'topic2Session', topicRecallList)
# corr = getQPPCorr('avg', 'WIG', 'queryWIG', 'topic2Session', topicRecallList)
# corr = getQPPCorr('max', 'WIG', 'queryWIG', 'topic2Session', topicRecallList)
