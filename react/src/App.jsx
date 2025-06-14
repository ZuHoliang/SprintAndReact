import { BrowserRouter as Router } from "react-router-dom";
import AuthProvider from "./contexts/AuthContext.jsx";
import AppRoutes from "./routes/AppRoutes";

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </Router>
  );
}

export default App;
