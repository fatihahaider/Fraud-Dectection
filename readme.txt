Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
clustering takes in a locations array and the number of clusters the resulting
graph should have. I throw any necessary exceptions and then create an edge
weighted graph with the given points of the locations. next i create a mst from
that graph. then i save the edges from the mst in an array, sort them, adn then
take the m-k smallest edges and create a new edge weighted graph from that. Once
that is complete I simply created a conncected componnets data structure from
that graph to use in the rest of the class

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
first throw any necessary excpetions such as incompatible array lengths and out
of bounds values. next i create an array of node objects and then populate the
array with each n row in input and each row's respective weight and label value.
then i sort the array. next before iterating through all the weaklearners, i c
calculate the initial total correct weight of points that are already on the
"correct" side of the line. then i go through the n points, predict their value,
if its the same i add that points weight to the initial sum, if not i subtract,
then at the end i check to see if the weight calulcated for the current line
is greater than the maxweight, and if it is then the values of sp dp and vp are
updated until the best weaklearner is found.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
     10          10          0.941               .191
     10          100         0.985               .871
     10          200         0.992               1.612
     10          400         0.995               3.027
     20          100         0.985               1.183
     30          100         0.989               1.571
     40          100         0.992               1.875
     50          100         0.99                2.185
     50          200         0.994               4.22
     50          300         0.994               6.254
     50          400         0.994               8.198
     100         100         0.97                3.327
     100         200         0.9725               6.46

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */

a k value around 40 and a t value around 300-400 seem to give the best results
and i did this by testing various values of t and k while the other one remained
constant. the higher and lower i went on one value i was able to track how it
was affecting the accuracy. the small value of t leads to low accuracy because
weaklearners are just that - weak. we are calculating values based off a sample
size and when t is too low it doesn't maximixe the correctness of the data as
training doesn't take a wide range/vareity of data. when k is too small, it
removes important pieces of data by clustering data that has a large range into
too similar catergories. You overlook important aspect of data. when k is too
large you don't cluster enough and therefor dont overlook any drastic outliers or
anything and thus the data doesnt show an accurate average of everything.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
just understanding how weak learner wokrs in general and how to translate that
into code.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
