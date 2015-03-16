Gaffe.configure do |config|
  config.errors_controller = {
    %r[^/api/] => 'Api::ErrorsController'
  }
end

Gaffe.enable!
