(ns light.csv
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def ^:private ^:const separator \,)
(def ^:private ^:const quote \")

(defn ^:private transform-data-value
  [value]
  (if (str/blank? (str value))
    nil
    value))

(defn ^:private parse-record
  ([s] (parse-record s []))
  ([[first-char & rest-chars :as s] result]
   (if (= first-char separator)
     (recur rest-chars (conj result nil))
     (let [delim (if (= first-char quote) quote separator)
           s-to-parse (->> (if (= delim separator) s rest-chars)
                           (apply str))
           entry (->> (take-while #(not= delim %)
                                  (seq s-to-parse))
                      (apply str)
                      transform-data-value)
           next-entry-index (-> (count entry) inc)
           has-more-to-process? (< next-entry-index (count s))]
       (if has-more-to-process?
         (recur (subs s-to-parse next-entry-index) (conj result entry))
         (conj result entry))))))

(defn parse-from-buffer
  "Given a buffer, it return a lazy sequence"
  [buffer & {:keys [headers? keyed?]}]
  (let [lines (line-seq buffer)
        headers (when headers? (parse-record (first lines)))
        headers (if keyed?
                  (map #(keyword (-> % str/trim str/lower-case (str/replace #" " "-"))) headers)
                  headers)]
    (if headers?
      (pmap #(zipmap headers (parse-record %)) (rest lines))
      (pmap #(parse-record %) lines))))

(defn ^:private perform-parse
  "Opens a reader and parses content"
  [source opts]
  (with-open [buffer (io/reader source)]
    (doall
      (apply parse-from-buffer buffer opts))))

(defn parse-string
  "Given a string, it return a map.
  `opts` can receive the following options: :headers?, :keyed?"
  [s & opts]
  (perform-parse (char-array s) opts))

(defn parse-file
  "Given a file path, returns a map.
  `opts` can receive the following options: :headers?, :keyed?"
  [file-path & opts]
  (perform-parse file-path opts))
