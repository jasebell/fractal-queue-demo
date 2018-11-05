(ns fractal.queue.demo
  (:require [fractal.queue.mainqueue :as q1]
            [fractal.queue.secondqueue :as q2]))

(defn run-queue-demo [nrange]
  (map (fn [i]
         (if (= 0 (rand-nth [0 1]))
           (q1/send-something-to-queue :id
                                       {:uuid (str (java.util.UUID/randomUUID))})
           (q2/send-something-to-queue :id
                                       {:uuid (str (java.util.UUID/randomUUID))}))) (range 1 nrange)))
