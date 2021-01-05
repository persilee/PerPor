class PageState{}

class InitializedState extends PageState {}

class DataFetchState<T> extends PageState {

  final T data;

  DataFetchState(this.data);

  bool get hasData => data != null;
}

class ErrorState extends PageState {}

class BusyState extends PageState {}