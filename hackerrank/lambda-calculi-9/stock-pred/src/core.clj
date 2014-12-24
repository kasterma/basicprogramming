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

(defn len-subseq [A [d M]]
  (let [[before after] (split-at d A)
        [ad & a-more] after
        bd (+ ad M)
        sa (take-while (fn [x] (<= ad x bd)) a-more)
        sb (take-while (fn [x] (<= ad x bd)) (reverse before))]
    (+ (inc (count sa)) (count sb))))

(defn main []
  (let [n (get-int)
        A (get-ints)
        Q (get-int)
        qq (for [q (range Q)] (get-ints))
        ans (map (partial len-subseq A) qq)]
    (doseq [a ans] (println a))))

;; 2
;; 3
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
  (main))
