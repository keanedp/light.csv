(ns light.cvs-test
  (:require [clojure.test :refer :all]
            [light.csv :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:import (java.io BufferedReader StringReader)))

;; test data

(def ^:const csv-without-headers (slurp (io/resource "test_files/without_headers.csv")))
(def ^:const csv-with-headers (slurp (io/resource "test_files/with_headers.csv")))

;; assert test results

(defn test-parse-without-headers
  [result]
  (let [[first-name last-name sex comments] (first result)]
    (is (= (count result) 2))
    (is (= "Eric" first-name))
    (is (= "Fowler" last-name))
    (is (= "Male" sex))
    (is (nil? comments))))

(defn test-parse-with-headers
  [result]
  (let [{:keys [first-name last-name sex comments]} (second result)]
    (is (= (count result) 2))
    (is (= "Laura" first-name))
    (is (= "Engle" last-name))
    (is (= "Female" sex))
    (is (= "Text that contains a comma, this should work..." comments))))

;; tests

(deftest parse-string-without-headers
  (let [result (parse-string csv-without-headers)]
    (test-parse-without-headers result)))

(deftest parse-string-with-headers
  (let [result (parse-string csv-with-headers :headers? true :keyed? true)]
    (test-parse-with-headers result)))

(deftest read-file-without-headers
  (let [result (parse-file (io/resource "test_files/without_headers.csv"))]
    (test-parse-without-headers result)))

(deftest read-file-with-headers
  (let [result (parse-file (io/resource "test_files/with_headers.csv") :headers? true :keyed? true)]
    (test-parse-with-headers result)))

(deftest read-string-buffer-without-headers
  (with-open [reader (BufferedReader. (StringReader. csv-without-headers))]
    (let [result (doall (parse-from-buffer reader))]
      (test-parse-without-headers result))))

(deftest read-string-buffer-with-headers
  (with-open [reader (BufferedReader. (StringReader. csv-with-headers))]
    (let [result (doall (parse-from-buffer reader :headers? true :keyed? true))]
      (test-parse-with-headers result))))

(deftest read-file-buffer-without-headers
  (with-open [reader (io/reader (clojure.java.io/resource "test_files/without_headers.csv"))]
    (let [result (doall (parse-from-buffer reader))]
      (test-parse-without-headers result))))

(deftest read-file-buffer-with-headers
  (with-open [reader (io/reader (clojure.java.io/resource "test_files/with_headers.csv"))]
    (let [result (doall (parse-from-buffer reader :headers? true :keyed? true))]
      (test-parse-with-headers result))))
