(ns pm.core
  (:require [aleph.http :as http]
            [manifold.stream :as s]
            [cognitect.transit :as t]))
            
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


;;testing server
(def ws @(http/websocket-client "ws://0.0.0.0:8080"))
@(s/put! ws "read-+8-+288")
(def a @(s/take! ws))
(close ws)


