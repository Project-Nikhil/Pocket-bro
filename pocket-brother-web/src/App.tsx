import { useState } from 'react';
import { Navbar } from './components/Navbar';
import { HeroSection } from './components/HeroSection';
import { DownloadCard } from './components/DownloadCard';
import { FeaturesSection } from './components/FeaturesSection';
import { InstallGuide } from './components/InstallGuide';
import { PrivacySection } from './components/PrivacySection';
import { FaqSection } from './components/FaqSection';
import { Footer } from './components/Footer';
import { BuildGuideModal } from './components/BuildGuideModal';
import { MobileStickyBar } from './components/MobileStickyBar';

export function App() {
  const [isBuildGuideOpen, setIsBuildGuideOpen] = useState(false);

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <Navbar onOpenBuildGuide={() => setIsBuildGuideOpen(true)} />
      
      <main style={{ flex: 1 }}>
        <HeroSection onOpenBuildGuide={() => setIsBuildGuideOpen(true)} />
        <DownloadCard onOpenBuildGuide={() => setIsBuildGuideOpen(true)} />
        <FeaturesSection />
        <InstallGuide />
        <PrivacySection />
        <FaqSection />
      </main>

      <Footer onOpenBuildGuide={() => setIsBuildGuideOpen(true)} />

      <MobileStickyBar />

      <BuildGuideModal
        isOpen={isBuildGuideOpen}
        onClose={() => setIsBuildGuideOpen(false)}
      />
    </div>
  );
}

export default App;
