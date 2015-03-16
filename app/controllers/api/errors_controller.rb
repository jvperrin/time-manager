class Api::ErrorsController < Api::ApiController
  include Gaffe::Errors

  # Render a simple JSON response containing the error “standard” code
  # plus the exception name and backtrace if we’re in development.
  def show
    output = { error: @rescue_response }
    output.merge! exception: @exception.inspect, backtrace: @exception.backtrace.first(10) if Rails.env.development? || Rails.env.test?
    render json: output, status: @status_code
  end
end
