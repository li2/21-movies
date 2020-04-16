# ABC Cinemas

This App is build on **Android Architecture Components (MVVM)** in **Kotlin** with following features:

- Two tabs (not showing, coming soon).
- Movies list and detail UI and API integration.
- Tablet layout support: show list and detail on the same screen.
- Shared element transition animation between list and detail.
- Video Player.

## Architecture

- **MVVM**:

    `ViewModel` is used to fetch data from `Repository` and to expose data as well as status (loading, success, error) to UI through `LiveData`, `Fragment` observers on the LiveData to render UI through `DataBinding`.
- **Retrofit + Moshi(Json) + Coroutine**:

    to consume web services data.
- **Postman mock server**:

    to provide mock data [/api/movies?type=comingSoon
](https://documenter.getpostman.com/view/5298582/SVfRv8vp?version=latest#fd182532-683b-43ee-b0b7-9e26d415facd)
- **Navigation**:

    Navigation graph, controller, safe arguments and auto generated directions classes are used to navigate between screens as well as passing data to screens.
- **Dependency Injection**:

    use [Kodein-DI](https://github.com/Kodein-Framework/Kodein-DI) for DI because it's easy to set up and use.


## Screenshots

<img src="https://user-images.githubusercontent.com/54767749/65827834-c6a41980-e2d7-11e9-81d7-09bf8ccdfbd9.png" width="666">
<img src="https://user-images.githubusercontent.com/54767749/65827836-c9067380-e2d7-11e9-86df-92d53115835c.png" width="666">


That's it.