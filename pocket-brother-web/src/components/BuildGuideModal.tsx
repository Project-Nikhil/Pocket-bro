import React from 'react';
import { X, Terminal } from 'lucide-react';

interface BuildGuideModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const BuildGuideModal: React.FC<BuildGuideModalProps> = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(0, 0, 0, 0.75)',
      backdropFilter: 'blur(8px)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      zIndex: 1000,
      padding: '20px',
    }}>
      <div style={{
        background: 'var(--surface-card)',
        border: '1px solid var(--border-medium)',
        borderRadius: 'var(--radius-xl)',
        maxWidth: '680px',
        width: '100%',
        maxHeight: '90vh',
        overflowY: 'auto',
        padding: '30px',
        position: 'relative',
        boxShadow: 'var(--shadow-elevated)',
      }}>
        {/* Close Button */}
        <button
          onClick={onClose}
          aria-label="Close modal"
          style={{
            position: 'absolute',
            top: '20px',
            right: '20px',
            width: '36px',
            height: '36px',
            borderRadius: '50%',
            background: 'var(--surface-elevated)',
            border: '1px solid var(--border-medium)',
            color: 'var(--text-cream)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            cursor: 'pointer',
          }}
        >
          <X size={18} />
        </button>

        {/* Modal Header */}
        <div style={{ marginBottom: '24px' }}>
          <span className="pill-badge" style={{ marginBottom: '10px' }}>
            <Terminal size={14} />
            Developer & Brother Guide
          </span>
          <h2 style={{ fontSize: '1.6rem', color: 'var(--text-cream)', marginTop: '4px' }}>
            How to Build the Genuine APK & Deploy
          </h2>
          <p style={{ fontSize: '0.9rem', color: 'var(--text-muted)', marginTop: '4px' }}>
            Because the AI Studio ZIP export contains source code rather than a pre-compiled binary, follow these steps to generate the official APK and deploy to Netlify.
          </p>
        </div>

        {/* Step-by-Step Instructions */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          {/* Step 1 */}
          <div style={{
            background: 'rgba(20, 17, 15, 0.6)',
            border: '1px solid var(--border-subtle)',
            borderRadius: 'var(--radius-md)',
            padding: '16px 18px',
          }}>
            <h4 style={{ color: 'var(--accent-amber)', fontSize: '1rem', marginBottom: '6px' }}>
              Step 1: Open the Android Project in Android Studio
            </h4>
            <p style={{ fontSize: '0.875rem', color: 'var(--text-muted)', lineHeight: 1.5 }}>
              Launch <strong>Android Studio</strong>, select <strong>Open</strong>, and choose your extracted project directory:
            </p>
            <div style={{
              background: '#0D0A08',
              padding: '8px 12px',
              borderRadius: '6px',
              fontFamily: 'monospace',
              fontSize: '0.825rem',
              color: 'var(--accent-amber)',
              marginTop: '8px',
            }}>
              e:\pocket bro\pocket-brother
            </div>
          </div>

          {/* Step 2 */}
          <div style={{
            background: 'rgba(20, 17, 15, 0.6)',
            border: '1px solid var(--border-subtle)',
            borderRadius: 'var(--radius-md)',
            padding: '16px 18px',
          }}>
            <h4 style={{ color: 'var(--accent-amber)', fontSize: '1rem', marginBottom: '6px' }}>
              Step 2: Build the APK
            </h4>
            <p style={{ fontSize: '0.875rem', color: 'var(--text-muted)', lineHeight: 1.5 }}>
              In Android Studio's top menu bar, click:
            </p>
            <div style={{
              background: '#0D0A08',
              padding: '8px 12px',
              borderRadius: '6px',
              fontFamily: 'monospace',
              fontSize: '0.825rem',
              color: 'var(--text-cream)',
              marginTop: '8px',
            }}>
              Build &gt; Build Bundle(s) / APK(s) &gt; Build APK(s)
            </div>
            <p style={{ fontSize: '0.825rem', color: 'var(--text-subtle)', marginTop: '8px' }}>
              Android Studio will compile all Compose screens, Room DAOs, and the Offline AI engine into a standalone APK.
            </p>
          </div>

          {/* Step 3 */}
          <div style={{
            background: 'rgba(20, 17, 15, 0.6)',
            border: '1px solid var(--border-subtle)',
            borderRadius: 'var(--radius-md)',
            padding: '16px 18px',
          }}>
            <h4 style={{ color: 'var(--accent-amber)', fontSize: '1rem', marginBottom: '6px' }}>
              Step 3: Copy to the Website Downloads Folder
            </h4>
            <p style={{ fontSize: '0.875rem', color: 'var(--text-muted)', lineHeight: 1.5 }}>
              Once Android Studio completes the build, click <strong>locate</strong> on the popup or navigate to:
            </p>
            <div style={{
              background: '#0D0A08',
              padding: '8px 12px',
              borderRadius: '6px',
              fontFamily: 'monospace',
              fontSize: '0.825rem',
              color: 'var(--text-subtle)',
              marginTop: '8px',
              wordBreak: 'break-all',
            }}>
              pocket-brother\app\build\outputs\apk\debug\app-debug.apk
            </div>
            <p style={{ fontSize: '0.875rem', color: 'var(--text-muted)', marginTop: '8px' }}>
              Copy this file into:
            </p>
            <div style={{
              background: '#0D0A08',
              padding: '8px 12px',
              borderRadius: '6px',
              fontFamily: 'monospace',
              fontSize: '0.825rem',
              color: 'var(--accent-amber)',
              marginTop: '4px',
              wordBreak: 'break-all',
            }}>
              pocket-brother-web\public\downloads\pocket-brother.apk
            </div>
          </div>

          {/* Step 4 */}
          <div style={{
            background: 'rgba(20, 17, 15, 0.6)',
            border: '1px solid var(--border-subtle)',
            borderRadius: 'var(--radius-md)',
            padding: '16px 18px',
          }}>
            <h4 style={{ color: 'var(--accent-amber)', fontSize: '1rem', marginBottom: '6px' }}>
              Step 4: Deploy to Netlify
            </h4>
            <p style={{ fontSize: '0.875rem', color: 'var(--text-muted)', lineHeight: 1.5 }}>
              Choose whichever deployment method you prefer:
            </p>
            <ul style={{ paddingLeft: '20px', fontSize: '0.85rem', color: 'var(--text-muted)', marginTop: '8px', display: 'flex', flexDirection: 'column', gap: '6px' }}>
              <li><strong>Option A (GitHub):</strong> Push your repository to GitHub and link it in Netlify. Netlify will run <code>npm run build</code> and publish <code>dist</code> using the pre-configured <code>netlify.toml</code>.</li>
              <li><strong>Option B (Netlify Drop):</strong> Run <code>npm run build</code> inside <code>pocket-brother-web</code>, then drag the generated <code>dist</code> folder into <a href="https://app.netlify.com/drop" target="_blank" rel="noopener noreferrer" style={{ color: 'var(--accent-amber)', textDecoration: 'underline' }}>app.netlify.com/drop</a>!</li>
            </ul>
          </div>

        </div>

        {/* Footer in modal */}
        <div style={{ marginTop: '24px', display: 'flex', justifyContent: 'flex-end' }}>
          <button
            onClick={onClose}
            className="btn-primary"
            style={{ padding: '10px 24px', fontSize: '0.9rem' }}
          >
            Got It!
          </button>
        </div>

      </div>
    </div>
  );
};
