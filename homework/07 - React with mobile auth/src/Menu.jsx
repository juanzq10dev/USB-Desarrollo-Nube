import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { useFirebaseAuth } from './hooks/useFirebaseAuth';

const Menu = () => {
    const navigate = useNavigate();
    const { user, loading, doLogout } = useFirebaseAuth();

    useEffect(() => {
        if (!user && !loading) {
            navigate('/');
        }
    }, [user, loading])


    return (<Navbar bg="dark" data-bs-theme="dark" expand="lg" className="bg-body-tertiary">
        <Container>
            <Navbar.Brand href="#home">Firebase example</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    <Nav.Link href="#home">Home</Nav.Link>
                    <Nav.Link href="#link">Link</Nav.Link>
                    <NavDropdown title={user?.email} id="basic-nav-dropdown">
                        <button className="dropdown-item" onClick={doLogout}>Logout</button>
                    </NavDropdown>
                </Nav>
            </Navbar.Collapse>
        </Container>
    </Navbar>);
}

export default Menu;