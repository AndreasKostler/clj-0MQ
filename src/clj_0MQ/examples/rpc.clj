(ns clj-0MQ.examples.rpc
  (:use [clj-0MQ.core :as zcore]
        [clj-0MQ.socket :as zsocket]))

(defn- string-to-bytes [s] (.getBytes s))
(defn- bytes-to-string [b] (String. b))

(defn handler [socket query]
  (let [query (bytes-to-string query)
        resultset (str "Received query: " query)]
    (zsocket/send- socket (string-to-bytes resultset))))

(defn- on-thread [f]
  (doto (Thread. #^Runnable f)
    (.start)))

(defn start-server []
  (let [ctx (zcore/make-context 1 false)
        socket (zcore/make-socket ctx zsocket/REP)]
    (on-thread
     #(do
        (zsocket/bind socket "tcp://lo:5555")
        (while true
          (let [query (zsocket/recv socket)]
            (handler socket query)))))))

(defn send-to-server [query]
  (zcore/with-context [cxt 1]
    (zcore/with-socket [socket cxt zsocket/REQ]
      (zsocket/connect socket "tcp://localhost:5555")
      (zsocket/send- socket (string-to-bytes query))
      (println (str "Received response: " (bytes-to-string (zsocket/recv socket)))))))