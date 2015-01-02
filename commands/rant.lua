function split(inputstr, sep)
  if sep == nil then
    sep = "%s"
  end
  local t={} ; i=1
  for str in string.gmatch(inputstr, "([^"..sep.."]+)") do
    t[i] = str
    i = i + 1
  end
  return t
end

local pattern = "<name-male> likes to <verb-transitive> <noun.plural> with <pron.poss-male> pet <noun-animal> on <timenoun.plural-dayofweek>."

if string.len(message:get_arguments()) > 0 then
  pattern = message:get_arguments();
end

local rant = http:post("http://berkin.me/rantbox/run", "code=" .. pattern .. "&nsfw=false")
local result = ""
local lines = split(rant, '\n')

for k, v in pairs(lines) do
  if string.match(v, "result") ~= nil then
    result = split(v, ":")[2]
    
    -- "trim"
    result = string.gsub(result, "^%s*(.-)%s*$", "%1")
    
    result = string.gsub(result, "\"", "")
  end
end

message:reply(result)