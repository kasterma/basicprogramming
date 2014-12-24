(ns core
  (:require [clojure.tools.logging :as log]
            [clojure.test :as test :refer [deftest is]]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.tools.trace :as trace]
            [criterium.core :as criterium]))

;; There are two main types of objects on this file, trees, denoted by t, t1,
;; t2, etc, and zippers, denoted by z, z1, z2, etc.
;;
;; The trees are numbers or maps with keywords as keys, and values are trees.
;;
;; The zippers are maps with keys :cur and :up.  :cur gives a tree and :up
;; gives a list of maps with :key and :siblings.  :key gives a keyword, :siblings a
;; tree.

;; Here is an example, t1 is a tree, (zipper t1) the zipper corresponding
;; to the tree.  Then some operations on the zipper are shown.
;;
;; t1
;;     {:e {:f 5}, :a {:b {:c 3, :d 4}}}
;; (zipper t1)
;;     {:cur {:e {:f 5}, :a {:b {:c 3, :d 4}}}, :up ()}
;; (down (zipper t1) :e)
;;     {:cur {:f 5}, :up ({:key :e, :siblings {:a {:b {:c 3, :d 4}}}})}
;; (down (down (zipper t1) :e) :f)
;;     {:cur 5, :up ({:key :f, :siblings {}} {:key :e, :siblings {:a {:b {:c 3, :d 4}}}})}
;; (up (down (down (zipper t1) :e) :f))
;;     {:cur {:f 5}, :up ({:key :e, :siblings {:a {:b {:c 3, :d 4}}}})}

;; We presume operations are only called when approriate (i.e. going sideways
;; only done with sibling an existing sibling.  In performance testing, only
;; want the core of the algorithm tested here.


;; Key zipper functions

(defn up [{:keys [cur up] :as z}]
  (let [[{:keys [key siblings] :as parent} & more-up]  up]
    {:cur (assoc siblings key cur) :up (if more-up more-up (list))}))

(defn down [{:keys [cur up] :as z} child-key]
  {:cur (get cur child-key)
   :up (cons {:key child-key :siblings (dissoc cur child-key)} up)})

(defn sideways [{:keys [cur up] :as z} sibling]
  (let [[{:keys [key siblings] :as parent} & more-up]  up]
    {:cur (get siblings sibling)
     :up (cons {:key sibling :siblings (assoc (dissoc siblings sibling) key cur)} more-up)}))

;; Helper functions

(defn children [z]
  (keys (:cur z)))

(defn siblings [{:keys [cur up] :as z}]
  (if (empty? up)
    []
    (vec (keys (:siblings (first up))))))

(defn get-cur [z]
  (:cur z))

;; Conversion between tree and zipper

(defn zipper [t]
  {:cur t :up (list)})

(defn tree [{:keys [cur up] :as z}]
  (if (not (empty? up))
    (tree (up z))
    cur))

;; Basic test

(def t1 {:a {:b {:c 3 :d 4}} :e {:f 5}})
(def z1 (zipper t1))

(deftest basics
  (is (= (children z1)
         '(:e :a)))
  (is (= (down z1 :a)
         {:cur {:b {:c 3, :d 4}}, :up '({:key :a, :siblings {:e {:f 5}}})}))
  (is (= (down (sideways (down z1 :a) :e) :f)
         {:cur 5, :up '({:key :f, :siblings {}} {:key :e, :siblings {:a {:b {:c 3, :d 4}}}})}))
  (is (= (siblings (down z1 :a))
         [:e]))
  (is (= (down (down z1 :a) :b)
         {:cur {:c 3, :d 4}, :up '({:key :b, :siblings {}} {:key :a, :siblings {:e {:f 5}}})}))
  (is (= (siblings (down (down z1 :a) :b))
         []))
  (is (= (get-cur (up (up (down (sideways (down z1 :a) :e) :f))))
         {:e {:f 5}, :a {:b {:c 3, :d 4}}}))
  (is (identical? (t1 :a) (get-cur (down z1 :a))))
  (is (not (identical? z1 (up (down z1 :a)))))
  (is (= z1 (up (down z1 :a)))))

