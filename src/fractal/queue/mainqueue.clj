(ns fractal.queue.mainqueue
  (:require [durable-queue :refer :all]
            [clojure.core.async :refer [go-loop <! timeout]]
            [taoensso.timbre :as log]))

;; The queue is persisted on disk, in this example it's being
(def my-test-queue (queues "/tmp/" {}))

(defn the-function-that-does-the-work [m]
  (log/info "Got the message! - " m))

(defn send-something-to-queue [k v]
  (put! my-test-queue k v))

(defn take-from-queue [k]
  (let [msgkey (take! my-test-queue k)]
    (do
      (the-function-that-does-the-work (deref msgkey))
      (complete! msgkey))))

(defn run-queue-loop []
  (if (not= 0 (:enqueued (stats my-test-queue)))
    (take-from-queue :id)))


(go-loop []
  (do
    (run-queue-loop)
    (<! (timeout 1000))
    (recur)))
