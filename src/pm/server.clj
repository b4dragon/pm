(ns pm.server
  (:require [aleph.http :as http]
            [manifold.stream :as s]
            [cognitect.transit :as t]))

(def non-websocket-request
  {:status 400
   :headers {"content-type" "application/text"}
   :body "Expected a websocket request."})


(defn echo-handler
  "The previous handler blocks until the websocket handshake completes, which unnecessarily
   takes up a thread.  This accomplishes the same as above, but asynchronously. "
  [req]
  (-> (http/websocket-connection req)
    (d/chain
      (fn [socket]
        (s/connect socket socket)))
    (d/catch
      (fn [_]
        non-websocket-request))))

(def handler
  (params/wrap-params
    (compojure/routes
      (GET "/echo" [] echo-handler)
      (route/not-found "No such page."))))

(defn start-server
  [port]
  (let [s http/start-server handler {:port port}
        input (chan)
        output (chan)]
    ;;make a connection input with req, output with response
    {:in input :out output}))

    
    

