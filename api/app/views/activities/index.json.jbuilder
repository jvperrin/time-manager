json.array!(@activities) do |activity|
  json.extract! activity, :id, :name, :color
  json.url user_url(activity, format: :json)
end
