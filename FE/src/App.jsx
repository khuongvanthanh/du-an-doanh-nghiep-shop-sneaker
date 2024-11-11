import "regenerator-runtime/runtime";
import { CommonProvider } from "./context/CommonContext";
import RouterProvider from "./routers/RouterProvider";

function App() {
  return (
    <CommonProvider>
      <RouterProvider />
    </CommonProvider>
  );
}
export default App;
