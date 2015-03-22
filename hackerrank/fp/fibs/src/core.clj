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

(def mem (atom {0 0 1 0 2 1}))

(defn fib [n]
  (if-let [v (log/spy (@mem n))]
    v
    (let [v (+ (fib (dec (dec n))) (fib (dec n)))]
      (swap! mem (fn [m] (assoc m n v)))
      v)))

(defn main []
  (let [n       (get-int)]
    (println (fib n))))

;; 2
;; 3
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))
