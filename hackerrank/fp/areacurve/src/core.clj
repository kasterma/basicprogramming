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

(defn eval-expr [as bs x]
  (let [coeff (map list as bs)]
    (apply +
      (map (fn [[a b]] (* a (Math/pow x b))) coeff))))

(defn auc [f from to]
  (let [eps 0.001
        pts (range from to eps)
        fs  (map f pts)]
    (apply + (map (fn [x] (* eps x)) fs))))

(defn area-circle [r]
  (* r r Math/PI))

(defn aiv [f from to]
  (let [eps 0.001
        pts (range from to eps)
        fs  (map f pts)]
    (apply + (map (fn [x] (* eps (area-circle x))) fs))))

(defn main []
  (let [as        (get-ints)
        bs        (get-ints)
        f         (fn [x] (eval-expr as bs x))
        [from to] (get-ints)]
    (println [as bs from to])
    (println (auc f from to))
    (println (aiv f from to))))

;; 2
;; 3
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))
