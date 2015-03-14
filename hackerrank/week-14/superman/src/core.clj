(ns core
  (:require [taoensso.timbre :as log]
            [taoensso.timbre.profiling :as prof]
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

(defn get-two-largest
  "Given list of nonnegative numers (more than two) return pair
   [largest next-largest]"
  [xs]
  (reduce
    (fn [[l1 l2] i] (cond (< l1 i) [i l1]
                          (< l2 i) [l1 i]
                          :else [l1 l2]))
    [0 0]
    xs))

(defn get-max [floor-cts I]
  (let [ground-floors (mapv (comp vector first) floor-cts)
        num-bld       (count floor-cts)
        num-floor     (count (first floor-cts))
        new-val
        (fn [acc building floor [l1 l2]]
          (+ (get-in floor-cts [building floor])
            (max (cond
                   (< floor I) 0  ;; cannot just down I floors
                   (= l1 (get-in acc [building (- floor I)])) l2
                   :else l1)
                 (get-in acc [building (dec floor)]))) )
        add-floors
        (fn [acc floor]
          (let [ls   (if (<= I floor)
                       (get-two-largest (mapv (fn [b] (get b (- floor I))) acc))
                       [0 0])
                newv (fn [acc b] (new-val acc b floor ls))]
            (reduce (fn [acc b]
                       (update-in acc [b] (fn [x] (conj x (newv acc b)))))
                    acc
                    (range num-bld))))]
    (reduce add-floors
      ground-floors
      (range 1 num-floor) ;; start at 1 since already have ground floor
      )))

(defn main []
  (let [[N H I]      (get-ints)
        floor-info   (doall (for [i (range N)] (vec (rest (get-ints)))))
        building-cts (fn [idxs]
                       (persistent!
                        (reduce
                         (fn [acc idx]
                          (assoc! acc (dec idx) (inc (get acc (dec idx)))))
                         (transient (vec (repeat H 0)))
                         idxs)))
        floor-cts    (mapv building-cts floor-info)]
  ;;(println [N H I floor-info floor-cts])
  (println (get-max floor-cts I))
  (println (apply max (map last (get-max floor-cts I))))))
;;
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase.txt"))]
 (main))
