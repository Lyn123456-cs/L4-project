from collections import defaultdict


def getTopicQPPAvg(queryQPPFile, topicSessionFile):
    session = []
    queryQPP = []
    with open(queryQPPFile) as f:
        for line in f:
            sessionID = line.split('-')
            session.append(sessionID[0])
            word = sessionID[1].split()
            queryQPP.append(float(word[1]))
    # print(queryNQC)
    # print(session)

    numQueryInSession = {}
    for i in range(100):
        s = "s" + str(i+1)
        count = session.count(s)
        numQueryInSession[s] = count
    # print(numQueryInSession)

    queryQPP2= queryQPP.copy()

    sessionQPP = defaultdict(set)
    currentIndex = 0
    for session in numQueryInSession:
        sessionQPP[session] = queryQPP2[currentIndex:currentIndex+numQueryInSession[session]]
        currentIndex += numQueryInSession[session]

    # print(sessionNQC)

    topic2session = defaultdict(set)
    with open(topicSessionFile) as f:
        for line in f:
            session, topic = line.split()
            topic2session[topic].add(session)

    # print(topic2session)
    topicQPP = defaultdict(set)

    for topic in topic2session:
        for s in topic2session[topic]:
            sessionID = "s" + str(s)
            set_ = sessionQPP[sessionID]
            topicQPP[topic] = topicQPP[topic].union(set_)
    # print(len(topicNQC['19']))

    topicAvgQPP = defaultdict(list)
    topicAvgQPPList = []
    print("topicNQC:", topicQPP)

    for topic in topicQPP:
        QPPSet = topicQPP[topic]
        QPPList = list(QPPSet)
        avgQPP = sum(QPPList)/len(QPPList)
        topicAvgQPP[topic] = avgQPP
        # print("avg:", avgQPP)
        topicAvgQPPList.append(avgQPP)
    # print(topicAvgNQC)
    return topicAvgQPPList


def getTopicQPPMax(queryQPPFile, topicSessionFile):
    session = []
    queryQPP = []
    with open(queryQPPFile) as f:
        for line in f:
            sessionID = line.split('-')
            session.append(sessionID[0])
            word = sessionID[1].split()
            queryQPP.append(float(word[1]))
    # print(queryNQC)
    # print(session)

    numQueryInSession = {}
    for i in range(100):
        s = "s" + str(i + 1)
        count = session.count(s)
        numQueryInSession[s] = count
    # print(numQueryInSession)

    queryQPP2 = queryQPP.copy()

    sessionQPP = defaultdict(set)
    currentIndex = 0
    for session in numQueryInSession:
        sessionQPP[session] = queryQPP2[currentIndex:currentIndex + numQueryInSession[session]]
        currentIndex += numQueryInSession[session]

    # print(sessionNQC)

    topic2session = defaultdict(set)
    with open(topicSessionFile) as f:
        for line in f:
            session, topic = line.split()
            topic2session[topic].add(session)

    # print(topic2session)
    topicQPP = defaultdict(set)

    for topic in topic2session:
        for s in topic2session[topic]:
            sessionID = "s" + str(s)
            set_ = sessionQPP[sessionID]
            topicQPP[topic] = topicQPP[topic].union(set_)
    # print(len(topicNQC['19']))

    topicMaxQPP = defaultdict(list)
    topicMaxQPPList = []
    print("topicNQC:", topicQPP)

    for topic in topicQPP:
        QPPSet = topicQPP[topic]
        QPPList = list(QPPSet)
        maxQPP = max(QPPList)
        topicMaxQPP[topic] = maxQPP
        # print(maxQPP)
        topicMaxQPPList.append(maxQPP)
    # print(topicAvgNQC)
    return topicMaxQPPList







