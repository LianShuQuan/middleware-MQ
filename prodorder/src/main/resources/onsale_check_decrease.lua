local result = 1

for i, key in ipairs(KEYS) do
local value = tonumber(redis.call('HGET', key, "quantity"))

if value == nil then
result = -1
break
elseif value < ARGV[1][i] then
result = 0
break
end
end

if result == 0 then
return result
elseif result == -1 then
return result
end

for i, key in ipairs(KEYS) do
local value = tonumber(redis.call('HGET', key, "quantity"))

redis.call('HSET', key, "quantity", value - ARGV[1][i])
end

return result