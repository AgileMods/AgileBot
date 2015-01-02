function onWebRequest(method, path, parameters, channels)
  if method == "GET" and path == "/echo" then
    if channels ~= nil then
      for k, v in pairs(channels) do
        v.send(parameters["echo"])
      end
    end
  end
end

register_callback(CALLBACK_HTTP_REQUEST, onWebRequest)