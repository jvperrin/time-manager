Rails.application.routes.draw do

  root 'dashboard#index'

  get '/about', to: 'dashboard#about', as: :about

  # Users
  resources :users, except: :destroy do
    resources :activities
  end

  # Sessions
  get    '/signin',  to: 'sessions#new',     as: :new_session
  post   '/signin',  to: 'sessions#create',  as: :create_session
  delete '/signout', to: 'sessions#destroy', as: :destroy_session

  namespace :api, defaults: { format: 'json' } do
    namespace :v1 do
      get   '/user', to: 'users#show'
      match '/user', to: 'users#update', via: [:put, :patch]

      post '/signin', to: 'sessions#create'

      resources :activities, except: [:new, :edit] do
        resources :activity_times, except: [:new, :edit]
      end
    end
  end

  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end