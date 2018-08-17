import sys
import random

with open ("basicMaxTest.stream", "w") as tf:
    for i in range (0, int(sys.argv[1])):
        tf.write("H(id=")
        tf.write(str(random.choice([1, 2, 3, 4])))
        tf.write(",value=")
        tf.write(str(random.randint(0, 100)))
        tf.write(")\n")
