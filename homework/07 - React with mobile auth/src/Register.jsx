import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { useState } from "react";
import { updateProfile } from "./repositories/UserRepository";
import { useNavigate } from "react-router-dom";
import { register } from "./repositories/AuthRepository";

const Register = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [name, setName] = useState('');
    const [lastname, setLastname] = useState('');
    const [age, setAge] = useState('');
    const submitRegisterForm = (e) => {
        e.preventDefault();
        register(email, password).then(() => {
            updateProfile(email, name, lastname, age).then(() => {
                navigate('/');
            });
        })
    }
    return (
        <Container>
            <Row>
                <Col>
                    <h1>Register</h1>
                    <Form onSubmit={submitRegisterForm}>
                        <Form.Group>
                            <Form.Label>Email</Form.Label>
                            <Form.Control required value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="Enter email" />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Password</Form.Label>
                            <Form.Control required value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="Enter password" />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Confirm Password</Form.Label>
                            <Form.Control required value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} type="password" placeholder="Enter password" />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Name</Form.Label>
                            <Form.Control required value={name} onChange={(e) => setName(e.target.value)} type="text" placeholder="Enter name" />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control required value={lastname} onChange={(e) => setLastname(e.target.value)} type="text" placeholder="Enter last name" />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Age</Form.Label>
                            <Form.Control required value={age} onChange={(e) => setAge(e.target.value)} type="number" placeholder="Enter age" />
                        </Form.Group>
                        <Button className="mt-2" variant="primary" type="submit">Register</Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default Register;