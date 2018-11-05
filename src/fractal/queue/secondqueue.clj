(ns fractal.queue.secondqueue
  (:require [durable-queue :refer :all]
            [clojure.core.async :refer [go-loop <! timeout]]
            [taoensso.timbre :as log]))

;; The queue is persisted on disk, in this example it's being
(def my-second-queue (queues "/tmp/" {}))

(defn the-function-that-does-the-work [m]
  (log/info "Got the message on second queue! - " m))

(defn send-something-to-queue [k v]
  (put! my-second-queue k v))

(defn take-from-queue [k]
  (let [msgkey (take! my-second-queue k)]
    (do
      (the-function-that-does-the-work (deref msgkey))
      (complete! msgkey))))

(defn run-queue-loop []
  (if (not= 0 (:enqueued (stats my-second-queue)))
    (take-from-queue :id)))

(go-loop []
  (do
    (run-queue-loop)
    (<! (timeout 1000))
    (recur)))
