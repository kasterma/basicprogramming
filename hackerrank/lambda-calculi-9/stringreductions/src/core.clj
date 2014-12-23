(ns core
  (:require [clojure.tools.logging :as log]
            [clojure.test :as test]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.tools.trace :as trace]
            [criterium.core :as criterium]))

(defn get-int [] (long (Integer/parseInt (read-line))))
(defn get-ints [] (map (fn [x] (long (Integer/parseInt x)))
                       (clojure.string/split (read-line) #" ")))

(defn main []
  (let [s (vec (read-line))
        so  (reduce (fn [acc n] (if (contains? acc n)
                                  acc
                                  (do (print n)
                                      (conj acc n))))
                    #{}
                    s)]
    (println "")))

(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))
