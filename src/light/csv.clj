(ns light.csv
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import (java.io StringReader BufferedReader)))

(def ^:private ^:const comma \,)
(def ^:private ^:const quote \")

(defn ^:private parse-record
  ([s] (parse-record s []))
  ([s result]
   (if (= (first s) comma)
     (recur (rest s) result)
     (let [delim (if (= (first s) quote) quote comma)
           s-to-parse (if (= delim comma) s (rest s))
           entry (take-while #(not= delim %)
                             (seq s-to-parse))]
       (if (< (+ (count entry) 1) (count s))
         (recur (subs (apply str s-to-parse) (+ (count entry) 1)) (conj result (apply str entry)))
         (conj result (apply str entry)))))))

(defn read-buffer
  "Given a buffer, it return a lazy sequence"
  [buffer & {:keys [headers? keyed?]}]
  (let [lines (line-seq buffer)
        headers (when headers? (parse-record (first lines)))
        headers (if keyed?
                  (map #(keyword (-> % str/trim str/lower-case (str/replace #" " "-"))) headers)
                  headers)]
    (if headers?
      (map #(zipmap headers (parse-record %)) (rest lines))
      (map #(parse-record %) lines))))

(defn parse-string
  "Given a string, it return a map"
  [s & opts]
  (let [buffer (BufferedReader. (StringReader. s))]
    (doall
      (apply read-buffer buffer opts))))

(defn read-file
  "Given a file path, returns a map"
  [file-path & opts]
  (with-open [buffer (io/reader file-path)]
    (doall
      (apply read-buffer buffer opts))))
