(defn make-initial-path [bot-pos first-pos dirty-positions]
  {:toVisit (remove #(= % first-pos) dirty-positions)
   :first first-pos
   :last first-pos
   :len (dist bot-pos first-pos)})

(defn make-extended-path [path pt]
  {:toVisit (remove #(= % pt) (:toVisit path))
   :first (:first path)
   :last pt
   :len (+ (:len path) (dist (:last path) pt))})

(defn get-best
  [bot-pos dirty-positions]
  (let [initial-queue
        (reduce (fn [pq pt] (add pq (make-inital-path bot-pos pt dirty-positions)))
                (make-priority-queue :len)
                dirty-positions)]
    (loop [queue initial-queue]
      (when (not (empty queue))
        (let [[best-path new-queue]  (pop-best queue)]
          (if (empty? (:toVisit best-path))
            (:first best-path)
            (recur (reduce (fn [pq pt] (add pq (make-entended-path best-path pt)))
                           new-queue
                           (:toVisit best-path)))))))))
