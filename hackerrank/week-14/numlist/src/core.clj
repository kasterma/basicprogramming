(ns core
  (:require [clojure.tools.logging :as log]
            [clojure.test :as test]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.tools.trace :as trace]
            [criterium.core :as criterium]))

(use 'clojure.repl)

(defn get-int [] (long (Integer/parseInt (read-line))))
(defn get-ints [] (map (fn [x] (long (Integer/parseInt x)))
                       (clojure.string/split (read-line) #" ")))

(defn ct [n]
  (/ (* n (inc n)) 2))

;;(trace/trace-vars step)

(defn main []
  (let [T       (get-int)]
    (doseq [i (range T)]
      (let [[N K]      (get-ints)
            xs         (map (fn [i] (< K i)) (get-ints))
            small-ct   (apply +
                         (map (comp ct count)
                              (remove first (partition-by identity xs))))]
        (println (- (ct N) small-ct))))))

;; 2
;; 3
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))
