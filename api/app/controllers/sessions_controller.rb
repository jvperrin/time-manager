class SessionsController < ApplicationController

  # GET /signin
  def new
    @continue = params[:continue] || request.referer
  end

  # POST /signin
  def create
    user = User.where(email: params[:session][:email]).first

    if user && user.authenticate(params[:session][:password])
      sign_in(user)

      flash[:success] = "You are now signed in"
      redirect_to safe_redirect(params[:continue])
    else
      @continue = params[:continue]
      @email = params[:session][:email]

      flash.now[:error] = "Invalid email/password combination"
      render :new
    end
  end

  # DELETE /signout
  def destroy
    sign_out
    flash[:success] = "You are now signed out"
    redirect_to safe_redirect(request.referer)
  end

  protected

  def safe_redirect(unsafe_url)
    uri = URI.parse(unsafe_url)

    # Check that the url has no host (relative) or that the host is the
    #   current server's host and the path begins with a slash
    if (!uri.host || uri.host == request.host) && uri.path[0] == "/"
      uri.path
    else
      root_path
    end

    # Redirect to the homepage if the url is invalid in any way
    rescue URI::InvalidURIError
      root_path
  end
end
