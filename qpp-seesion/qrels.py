from collections import defaultdict

qrels = defaultdict(dict)

with open('qrelsFile') as f:
    for line in f:
        topic, dummy, doc, rel = line.split()
        qrels[topic][doc] = rel
print(qrels)

