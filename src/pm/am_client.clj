(ns pm.am-client
  (:require [aleph.http :as http]
            [manifold.stream :as s]
            [clojure.core.async :as async :refer [<! >! <!! >!! chan]]))


(def server-addr "ws://0.0.0.0:8080")

(defn connect-server
  []
  (let [ws @(http/websocket-client server-addr)
        input (chan)
        output (chan)]
    (loop []
      (when-let [req (<!! input)]
        @(s/put! ws req)
        (when-let [rsp @(s/take! ws)]
                  (>!! output rsp)))
      (recur))
    {:in-ch input :out-ch output}))

    
