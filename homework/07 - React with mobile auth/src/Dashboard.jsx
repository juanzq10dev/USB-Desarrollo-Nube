import { Button, Col, Container, Form, Row } from "react-bootstrap";
import Menu from "./Menu";
import { useFirebaseAuth } from "./hooks/useFirebaseAuth";
import { useState } from "react";
import { addPhoneNumber } from "./repositories/UserRepository";

const Dashboard = () => {
    const { profile } = useFirebaseAuth();
    const [phoneNumber, setPhoneNumber] = useState('')
    const [contactName, setContactName] = useState('')


    const postPhoneNumber = async () => {
        console.log(profile?.email)
        const bool = await addPhoneNumber(profile?.email, contactName, phoneNumber)
        if (bool) {
            alert("Contacto agregado")
        } else {
            alert("Contacto no agregado")
        }
    }
    return (
        <>
            <Menu />
            <Container>
                <Row>
                    <Col>
                        <h1>Dashboard</h1>
                        <p>Welcome {profile?.name} {profile?.lastName}</p>
                        <Form onSubmit={postPhoneNumber}>
                            <Form.Group>
                                <Form.Label>Name</Form.Label>
                                <Form.Control required value={contactName} onChange={(e) => setContactName(e.target.value)} type="text" placeholder="Enter contact name" />
                            </Form.Group>
                            <Form.Group>
                                <Form.Label>Phone Number</Form.Label>
                                <Form.Control required value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} type="number" placeholder="Enter phone number" />
                            </Form.Group>
                            <Button className="mt-2" variant="primary" type="submit">Register</Button>

                        </Form>
                    </Col>
                </Row>
            </Container>
        </>
    );
}

export default Dashboard;