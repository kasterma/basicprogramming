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


;;(trace/trace-vars step)

(defn exp [x]
  (apply +
    (reduce (fn [acc i] (into acc [(/ (* (last acc) x) i)]))
      [1]
      (range 1 10))))

(defn main []
  (let [T       (get-int)]
    (doseq [i (range T)]
      (let [x (Double/parseDouble (read-line))]
        (println (exp x))))))

;; 2
;; 3
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))
