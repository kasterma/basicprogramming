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

(defn print-ans [m]
  (let [sks  (sort (keys m))
        s    (mapcat (fn [k] [k (m k)]) sks)]
    (println (clojure.string/join " " s))))

(defn main []
  (let [n (get-int)
        ls (for [i (range n)]
             (into {} (map vec (partition 2 (get-ints)))))
        pres  (reduce (fn [acc m] (clojure.set/intersection acc (set (keys m))))
                      (set (keys (first ls)))
                      (rest ls))
        lred (map (fn [m] (select-keys m (vec pres))) ls)
        ans  (reduce (partial merge-with min) lred)]
    (print-ans ans)))

;; 7 1
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))

;; 3 2 5 3
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase2.txt"))]
  (main))
