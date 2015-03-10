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

(defn step
  [k ns n acc]
  (if (or (zero? k) (<= n 1)) ;; changing to (zero? n) gives runtime error
    (into acc ns)
    (let [idx (.indexOf ns n)]
      (if (zero? idx)
        (recur k (vec (rest ns)) (dec n) (conj acc n))
        (let [nns (rest (assoc ns idx (first ns)))]
          (recur (dec k) (vec nns) (dec n) (conj acc n)))))))

(defn random-swap
  [ns]
  (let [n (count ns)
        i (rand-int n)
        j (rand-int (dec n))
        j (if (== j i) (inc j) j)
        ati (get ns i)
        atj (get ns j)]
    (assoc (assoc ns j ati) i atj)))

(defn lex-leq
  "assume same length"
  [[x & xs] [y & ys]]
  (or (< x y)
      (and (= x y)
           (or (and (nil? xs) (nil? ys))
               (recur xs ys)))))

(defn k-swaps [ns k]
  (if (zero? k)
    (vec ns)
    (k-swaps (random-swap ns) (dec k))))

(defn test-1
  "Run a bunch of times, always found it"
  [ns k]
  (let [res (step k ns (count ns) [])]
    (loop [i 0]
      (if (= res (k-swaps ns k))
        i
        (recur (inc i))))))

(defn test-2
  [ns k ct]
  (let [res (step k ns (count ns) [])]
    (loop [i 0]
      (if (= i ct)
        false
        (let [res2 (k-swaps ns k)]
          (if (not (lex-leq res2 res))
            [res res2]
            (recur (inc i))))))))

(trace/trace-vars step)

(defn main []
  (let [[N K]   (get-ints)
        ns      (if (pos? N) (vec (get-ints)))]
   ;; (println [N K ns])
   (if (zero? N)
    (println "")
    (println (clojure.string/join " " (step K ns (count ns) []))))))

;; simple and too slow: just compute hts per day

;; 2
;; 3
(binding [*in* (java.io.BufferedReader. (java.io.FileReader. "testcase2.txt"))]
  (main))
