# About This Project
A project of abnormal detecting by Multivariate-Gaussian Algorithm.

**There are Two parts :** 
* one is part of learning using multi-gaussian algorithm;
* other is part of detecting basing on the result of learning.

## part of learning using multi-gaussian algorithm

This part starts by **GaussLearner.java**.

**Steps of learning:**

* calculate means using all training data. Structure of means is a vector, each item is for one property of training data.
* In addition, value of each data is changed using lg(x) .
* calculate covariance for training data using means.
* do a inversion for the covariance and get a inverse matrix.
* calculate the suffix of p(x) formula.


**Things should to be pay attention:**

The algorithm needs very high accuracy, so BigDecimal is used instead of Double. But BigDecimal can't ensure a accuracy of 100% since BigDecimal's di


## part of detecting basing on the result of learning

This part starts by **GaussDetector.java**.
