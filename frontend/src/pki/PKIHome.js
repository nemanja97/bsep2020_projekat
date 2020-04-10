import React, { useEffect, useState } from 'react';
import Tree from 'react-animated-tree';
import { CertificateService } from '../services/CertificateService';
import moment from 'moment';
import { useHistory } from 'react-router-dom';

function PKIHome() {
    const [data, setData] = useState({})
    const history = useHistory()

    const generateTree = (nodeData) => {
        let children = [];
        if (nodeData && nodeData.issued) {
            nodeData.issued.forEach(child => {
                children.push(generateTree(child));
            });
        }

        const config = open => ({
            from: {},
            to: {
                height: open ? 'auto' : 0,
            },
        })

        return <Tree springConfig={config} open key={nodeData.id} content={generateContent(nodeData)}>{children.length > 0 && children}</Tree>;
    }

    const generateContent = (nodeData) => {
        const revoked = nodeData.revoked;
        const expired = checkExpired(nodeData);
        const isCA = nodeData.isCA;
        let tag = '';
        if (revoked) {
            tag = <span style={{ color: 'red' }}>[REVOKED]</span>;
        } else if (expired) {
            tag = <span style={{ color: 'red' }}>[EXPIRED]</span>;
        }
        const contentStyle = {
            cursor: 'pointer',
        };
        const buttonStyle = {
            marginLeft: 24
        }
        return <span>{tag}<span onClick={handleClick(nodeData.alias)} style={contentStyle}> {nodeData.commonName}</span>
            {(!revoked && !expired && isCA) && <button style={buttonStyle} className="btn btn-primary" onClick={handleAdd(nodeData.alias)}>issue new</button>}
        </span>;
    }

    const checkExpired = (nodeData) => {
        const now = moment();
        const start = moment(nodeData.startDate);
        const end = moment(nodeData.endDate);
        if (now.isBefore(start) || now.isAfter(end)) {
            return true;
        }
        return false;
    }

    const handleClick = (id) => () => {
        history.push(`/pki/${id}`);
    }

    const handleAdd = (id) => (e) => {
        e.stopPropagation();
        history.push(`/pki/${id}/addCertificate`);
    }

    const treeStyle = {
        padding: 16,
        fontFamily: 'arial',
        fontSize: 20
    };

    useEffect(() => {
        CertificateService.getAll()
            .then(response => {
                setData(response.data)
            })
    }, [])

    return (
        <div style={treeStyle}>
            All certificates:
            {generateTree(data)}
        </div>
    )
}

export default PKIHome;