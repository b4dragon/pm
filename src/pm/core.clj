(ns pm.core
  (:require [aleph.http :as http]
            [manifold.stream :as s]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


;;testing server
(def ws @(http/websocket-client "ws://0.0.0.0:8080"))

@(s/put! ws "hi")
